# 🌿 Vitatech - Back-end API

A API oficial do **Vitatech**, uma plataforma de acompanhamento nutricional e esportivo que conecta nutricionistas e pacientes. Construída com arquitetura RESTful, a aplicação gere planos alimentares, atividades físicas (com localização e multiplicadores de esforço) e chat em tempo real.

---

## ✨ Funcionalidades Principais

* **🔐 Autenticação e Segurança (Foco na LGPD):**
* Perfis de acesso rigorosos (`PATIENT`, `NUTRITIONIST`, `ADMIN`).
* Criptografia de senhas nativa com BCrypt.
* Autenticação via tokens JWT (JSON Web Tokens).


* **🤝 Gestão de Vínculos:**
* Fluxo de aprovação e acompanhamento direto entre pacientes e nutricionistas.


* **🍏 Diário Alimentar:**
* Catálogo base de alimentos brasileiros com cálculo automático de macronutrientes (Proteínas, Carboidratos, Gorduras e Calorias).
* Registo de refeições diárias detalhadas.


* **🏃 Atividades Físicas & Gasto Calórico:**
* Catálogo de exercícios com cálculo de calorias por minuto.
* Sistema de **Multiplicadores de Esforço Geográfico** (Ex: Correr na Areia Fofa exige mais esforço do que no Asfalto).


* **💬 Comunicação em Tempo Real:**
* Chat integrado via **WebSockets** para comunicação direta entre o paciente e o profissional de saúde.



---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 3
* **Segurança:** Spring Security + JWT
* **Banco de Dados:** PostgreSQL (Hospedado no Neon)
* **Migrações:** Flyway
* **Persistência de Dados:** Spring Data JPA / Hibernate
* **Comunicação em Tempo Real:** Spring WebSockets
* **Build Tool:** Maven
* **Deploy:** Docker (Render)

---

## 🚀 Como Executar o Projeto Localmente

### Pré-requisitos

* [Java 21](https://jdk.java.net/21/) instalado.
* [Maven](https://maven.apache.org/) instalado.
* Uma instância do PostgreSQL a rodar localmente ou acesso ao Neon.

### Passos para Instalação

1. **Clone o repositório:**
> `git clone [https://github.com/SeuUsuario/vitatech-backend.git](https://github.com/SeuUsuario/vitatech-backend.git)`
> `cd vitatech-backend`


2. **Configure as Variáveis de Ambiente:**
Configure as credenciais do seu banco de dados no arquivo `src/main/resources/application.properties` ou exporte-as no seu ambiente:
* `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/vitatech_db`
* `SPRING_DATASOURCE_USERNAME=seu_usuario`
* `SPRING_DATASOURCE_PASSWORD=sua_senha`
* `SPRING_FLYWAY_URL=${SPRING_DATASOURCE_URL}`
* `SPRING_FLYWAY_USER=${SPRING_DATASOURCE_USERNAME}`
* `SPRING_FLYWAY_PASSWORD=${SPRING_DATASOURCE_PASSWORD}`


3. **Inicie a Aplicação:**
> `mvn spring-boot:run`


*Nota: No primeiro arranque, o Flyway executará automaticamente as migrações (`V1` para criar tabelas e `V2` para popular o banco de dados com os locais, alimentos e atividades padrão).*

---

## 🗄️ Estrutura do Banco de Dados (Database Seeding)

O projeto utiliza o **Flyway** para versionamento. O banco de dados já nasce populado em produção com:

* **Locais de Treino:** Focados na topografia do Rio de Janeiro (Areia Fofa, Trilha com Subida, Asfalto, etc.) com multiplicadores de impacto calórico.
* **Alimentos:** Catálogo base focado na nutrição brasileira (Feijão, Tapioca, Whey Protein, Peito de Frango, etc.) com informações exatas de macronutrientes por 100g.
* **Exercícios:** Lista de atividades (Musculação, HIIT, Jiu-Jitsu, Futvôlei) com gastos calóricos base parametrizados.

---

## ☁️ Deploy em Produção (Render)

O deploy está automatizado através do **Render** utilizando `Dockerfile`.
As variáveis de ambiente configuradas no painel do Render incluem:

| Variável | Descrição |
| --- | --- |
| `PORT` | Porta de exposição do contêiner Docker (Ex: `8080`) |
| `SERVER_PORT` | Força o Spring Boot a ouvir a porta correta |
| `SPRING_DATASOURCE_URL` | URL de conexão ao pool do Neon (com `sslmode=require`) |
| `SPRING_DATASOURCE_USERNAME` | Usuário do DB Neon |
| `SPRING_DATASOURCE_PASSWORD` | Senha do DB Neon |
| `SPRING_FLYWAY_*` | Credenciais idênticas replicadas para o Flyway |

**Frontend Integrado:** O front-end (React/Vite) está hospedado no **Vercel** e o back-end possui políticas globais de **CORS** configuradas no `SecurityConfig.java` para aceitar requisições externas em segurança.

---

## 📜 Licença

Este projeto é desenvolvido para a plataforma Vitatech. Todos os direitos reservados.