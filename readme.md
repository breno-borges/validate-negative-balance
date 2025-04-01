# Validador de Saldo Negativo

## Descrição
O **Validador de Saldo Negativo** é uma aplicação desenvolvida para auxiliar o time de operações na análise de chamados em que um cliente apresenta saldo negativo. 
O objetivo é identificar as causas do saldo negativo e sugerir uma ou mais ações recomendadas com base na análise realizada. A análise é feita a partir de um arquivo
xls, xlsx ou csv, onde um novo arquivo é criado com base no selecionado e a análise é feita em cima desse novo arquivo que a aplicação cria.

## Tecnologias Utilizadas
- **Linguagem:** Java 8
- **Interface Gráfica:** Java Swing

## Requisitos
- **Java 8** ou superior instalado na máquina

## Como Executar
1. Clone este repositório:
   <pre>git clone https://github.com/breno-borges/validate-negative-balance.git</pre>
2. Acesse o diretório do projeto:
   <pre>cd validate-negative-balance</pre>
3. Compile o projeto: <pre>javac -d bin -sourcepath src src/Main.java</pre>
4. Execute a aplicação: <pre>java -cp bin Main</pre>

## Contribuição
Contribuições são bem-vindas! Se quiser sugerir melhorias ou corrigir problemas, siga estes passos:

1. Faça um fork do repositório

2. Crie uma branch para a sua feature: <pre>git checkout -b minha-feature</pre>

3. Faça commit das suas alterações: <pre>git commit -m "Adiciona nova funcionalidade"</pre>

4. Envie para o repositório remoto:<pre>git push origin minha-feature</pre>

5. Abra um Pull Request

## Licença
Este projeto está sob a licença MIT. Para mais detalhes, consulte o arquivo LICENSE.

Feito com 💻 por Breno Borges 🚀
