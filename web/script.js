function tabela(colunas, linhas) {
  const thead = `<thead><tr>${colunas.map((c) => `<th>${c}</th>`).join("")}</tr></thead>`;
  const tbody = `<tbody>${linhas
    .map((linha) => `<tr>${linha.map((celula) => `<td>${celula}</td>`).join("")}</tr>`)
    .join("")}</tbody>`;
  return `<table>${thead}${tbody}</table>`;
}

function ligarAbas() {
  const tabs = document.querySelectorAll(".tab");
  const panels = document.querySelectorAll(".panel");

  tabs.forEach((tab) => {
    tab.addEventListener("click", () => {
      const alvo = tab.dataset.tab;

      tabs.forEach((item) => {
        item.classList.toggle("is-active", item === tab);
        item.setAttribute("aria-selected", item === tab ? "true" : "false");
      });

      panels.forEach((panel) => {
        const ativo = panel.id === alvo;
        panel.classList.toggle("is-active", ativo);
        panel.hidden = !ativo;
      });
    });
  });
}

function preencher(dados) {
  document.getElementById("tabela-lista").innerHTML = tabela(
    ["Nome", "Data de nascimento", "Função", "Salário"],
    dados.lista.map((f) => [f.nome, f.dataNascimento, f.funcao, f.salario])
  );

  document.getElementById("tabela-aumento").innerHTML = tabela(
    ["Nome", "Salário"],
    dados.aumento.map((f) => [f.nome, f.salario])
  );

  document.getElementById("grupos-funcao").innerHTML = dados.porFuncao
    .map(
      (grupo) => `
        <article class="grupo">
          <h3>${grupo.funcao}</h3>
          <ul>${grupo.nomes.map((nome) => `<li>${nome}</li>`).join("")}</ul>
        </article>
      `
    )
    .join("");

  document.getElementById("tabela-aniversario").innerHTML = tabela(
    ["Nome", "Data de nascimento"],
    dados.aniversariantes.map((f) => [f.nome, f.dataNascimento])
  );

  document.getElementById("tabela-idade").innerHTML = tabela(
    ["Nome", "Idade"],
    dados.porIdade.map((f) => [f.nome, String(f.idade)])
  );

  document.getElementById("tabela-alfabetica").innerHTML = tabela(
    ["Nome"],
    dados.alfabetica.map((nome) => [nome])
  );

  document.getElementById("soma-salarios").textContent = `R$ ${dados.somaSalarios}`;
  document.getElementById("hint-minimo").textContent =
    `Salário mínimo considerado: R$ ${dados.salarioMinimo}.`;

  document.getElementById("tabela-minimo").innerHTML = tabela(
    ["Nome", "Salário", "Equivalência"],
    dados.equivalencia.map((f) => [f.nome, f.salario, f.equivalencia])
  );
}

async function carregarDoJava() {
  const aviso = document.getElementById("aviso");
  try {
    const resposta = await fetch("/api/resultados");
    if (!resposta.ok) {
      throw new Error("Falha ao buscar resultados no Java");
    }
    preencher(await resposta.json());
  } catch (erro) {
    aviso.hidden = false;
    aviso.textContent =
      "Abra a interface pelo Java (java Main) em http://localhost:8080. O JS não calcula os dados.";
    console.error(erro);
  }
}

ligarAbas();
carregarDoJava();
