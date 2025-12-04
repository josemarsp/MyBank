# MyBank Android

Para rodar o aplicativo é necessário subir o mockserver **Mockoon** usando o arquivo de configuração `Mockoon-MyBank.json` que está na raiz do projeto.
## Visão geral

- Projeto Android em Kotlin usando Compose, retrofits com Moshi e arquitetura MVVM limpa.
- Dependências e módulos estão organizados em `app` e `selfievalidation`, com Koin para injeção de dependências.

![layout.png]
!(layout.png)

## Como iniciar

1. Abrir o projeto no Android Studio (nível de projeto `MyBank`).
2. Executar o Mockoon localmente importando `Mockoon-MyBank.json`.
3. Certificar-se de que o mock está rodando em `http://localhost:82/` (mesmo endereço dos servidores definidos).
4. Buildar e rodar a aplicação no emulador ou dispositivo.

## Estrutura

- `app`: funcionalidade principal com login, API de conta e módulos de rede.
- `selfievalidation`: fluxo de validação facial com cameraX e fluxo completo para captura e validação e um módulo apartado.


## Testes

- `app/src/androidTest`: contém um teste instrumentado de exemplo (`ExampleInstrumentedTest`) que valida o package name da aplicação usando o runner `AndroidJUnit4`.



