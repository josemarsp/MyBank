## Selfie Validation Module Plan

### 1. Estrutura de pacotes

- `com.example.mybank.selfievalidation.domain`
  - `model` (caso precise entidades locais)
  - `repository` (interface `SelfieValidationRepository`)
  - `usecase` (`ValidateSelfieUseCase`)

- `com.example.mybank.selfievalidation.data`
  - `remote`
    - `SelfieValidationApi` (Retrofit)
    - DTOs (`SelfieRequestDto`, `SelfieResponseDto`)
  - `repository`
    - `SelfieValidationRepositoryImpl`
  - `util`
    - `SelfieBase64Converter` (Bitmap/ImageProxy → Base64)
    - `CameraImageProvider` (se necessário)

- `com.example.mybank.selfievalidation.presentation`
  - `viewmodel`
    - `SelfieValidationViewModel`
  - `ui`
    - `CaptureSelfieScreen`
    - `SelfieResultScreen`

- `com.example.mybank.selfievalidation.navigation`
  - `SelfieValidationRoutes` (opcional)

### 2. Módulo Koin (`SelfieValidationModule`)

- **Network**
  - `single { get<Retrofit>().create(SelfieValidationApi::class.java) }`
- **Utilities**
  - `single { SelfieBase64Converter() }`
- **Repository**
  - `factory<SelfieValidationRepository> { SelfieValidationRepositoryImpl(get(), get()) }`
- **UseCase**
  - `factory { ValidateSelfieUseCase(get()) }`
- **ViewModel**
  - `viewModel { SelfieValidationViewModel(get(), get()) }`

### 3. Integração

- Adicionar `selfieValidationModule` à lista `bankModules` em `AppModules.kt`.
- Incluir rotas em `BankRoutes` e novos `composable` em `BankNavHost`.
- Lançar fluxo: após login navegar para `CaptureSelfieScreen`, enviar selfie e mostrar `SelfieResultScreen`.

