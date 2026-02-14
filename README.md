# TvOff
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpack-compose&logoColor=white)
![Supabase](https://img.shields.io/badge/Supabase-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white)

Um aplicativo Android moderno para gerenciar e compartilhar seu perfil musical. Crie seu perfil único, escolha 5 álbuns favoritos e registre todo seu histórico musical.

## Tecnologias
- **Linguagem**: Kotlin
- **UI**: Jetpack Compose
- **Backend**: Supabase (PostgreSQL, Auth, Storage)
- **Arquitetura**: MVVM
- **Navegação**: Navigation Compose
- **Imagens**: Coil
- **Async**: Coroutines + Flow

## Pré-requisitos
- Android Studio Hedgehog | 2023.1.1 ou superior
- JDK 11 ou superior
- Android SDK 36 (mínimo: SDK 24 / Android 7.0)
- Conta no [Supabase](https://supabase.com)

## Configuração do Projeto
### 1. Clone o Repositório

```bash
git clone https://github.com/seu-usuario/tvoff.git
cd tvoff
```

### 2. Configure as Chaves do Supabase
**IMPORTANTE**: As chaves do Supabase NÃO estão no repositório.

1. **Obtenha suas chaves do Supabase**:
   - Crie um novo projeto
   - Copie a `URL` do projeto e a chave `anon/public`

2. **Configure o arquivo local.properties**:
   - Na raiz do projeto, copie o arquivo `local.properties.example`:
   
   ```bash
   cp local.properties.example local.properties
   ```
   
   - Abra o arquivo `local.properties` e adicione suas chaves:
   
   ```properties
   SUPABASE_URL=https://seu-projeto.supabase.co
   SUPABASE_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```

3. **Sincronize o projeto no Android Studio**:
   - Clique em `File` > `Sync Project with Gradle Files`

### 3. Execute o Projeto

1. Conecte um dispositivo Android ou inicie um emulador
2. Pressione `Shift + F10`
3. O app será compilado e instalado no dispositivo

## Estrutura do Projeto

```
tvoff/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/matheus/tvoff/
│   │       │   ├── data/
│   │       │   │   └── supabase/
│   │       │   │       └── SupabaseConfig.kt
│   │       │   ├── ui/
│   │       │   │   ├── home/
│   │       │   │   │   └── HomeScreen.kt
│   │       │   │   ├── perfil/
│   │       │   │   │   └── PerfilScreen.kt
│   │       │   │   └── theme/
│   │       │   │       └── Theme.kt
│   │       │   └── MainActivity.kt
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
├── local.properties.example
├── .gitignore
└── README.md
```

## Para Contribuir

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## Autor
**Matheus de Andrade**
