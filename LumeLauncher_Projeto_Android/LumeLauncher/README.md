# Lume Launcher

Launcher Android independente, feito em Kotlin + Jetpack Compose. O projeto tem identidade, pacote e código próprios; não contém código, marca ou recursos extraídos de outro aplicativo.

## O que já funciona

- Pode ser escolhido pelo Android como aplicativo de tela inicial (`CATEGORY_HOME`).
- Também aparece na lista comum de aplicativos (`CATEGORY_LAUNCHER`).
- Lista atividades de aplicativos iniciáveis no perfil atual do usuário.
- Abre um aplicativo ao tocar no respectivo ícone.
- Pesquisa aplicativos pelo nome.
- Adiciona/remove favoritos com toque longo; favoritos ficam salvos localmente.
- Atualiza a lista ao voltar à tela inicial ou tocar no botão de atualizar.
- Interface própria em tema escuro, com relógio, data e cores Lume.
- Não pede `QUERY_ALL_PACKAGES`, contatos, localização, calendário, notificações ou acessibilidade.

## Requisitos

- JDK 17.
- Android SDK Platform 36.
- Conexão com a internet na primeira compilação para baixar Gradle e dependências.

Versões fixadas no projeto:

| Componente | Versão |
|---|---:|
| Android Gradle Plugin | 8.11.1 |
| Gradle | 8.13 |
| Kotlin | 2.2.21 |
| Compose BOM | 2026.09.00 |
| Activity Compose | 1.13.0 |
| compileSdk / targetSdk | 36 / 36 |
| minSdk | 26 (Android 8.0) |

## Compilar pelo GitHub Codespaces

O projeto está na pasta `LumeLauncher_Projeto_Android/LumeLauncher`, que contém o arquivo `settings.gradle.kts`.

1. Abra o repositório no GitHub.
2. Clique em **Code → Codespaces → Create codespace on main**.
3. Aguarde o VS Code web abrir.
4. Abra o terminal e execute:

```bash
cd LumeLauncher_Projeto_Android/LumeLauncher
java -version
bash ./gradlew assembleDebug
```

O ambiente precisa ter JDK 17 e Android SDK Platform 36. O script `gradlew` deste repositório baixa a distribuição oficial Gradle 8.13 para o cache do usuário; não é necessário ter Android Studio. O primeiro build também baixa os plugins e bibliotecas do projeto.

APK de depuração esperado:

`app/build/outputs/apk/debug/app-debug.apk`

## Compilação automática pelo GitHub Actions

O workflow **Build Android APK** está em:

`.github/workflows/build-android.yml`

Ele executa automaticamente em cada push para a branch `main` e também pode ser iniciado manualmente:

1. Abra a aba **Actions** do repositório.
2. Selecione **Build Android APK**.
3. Clique em **Run workflow** e escolha a branch `main`.
4. Abra a execução e aguarde o job **Compile Lume Launcher** terminar.
5. Na seção **Artifacts**, baixe `lume-launcher-debug-apk`.

O artefato fica disponível por 14 dias. A compilação ainda precisa ser confirmada pela execução do workflow; a existência do arquivo YAML, por si só, não comprova que o APK foi gerado.

## Abrir no Android Studio

1. Baixe ou clone o repositório.
2. Abra a pasta `LumeLauncher_Projeto_Android/LumeLauncher` (a que contém `settings.gradle.kts`).
3. Aguarde a sincronização do Gradle.
4. Se necessário, instale **Android SDK Platform 36**.
5. Execute em um aparelho ou emulador com Android 8.0 ou superior.

Também é possível compilar pelo terminal:

- Windows: `gradlew.bat assembleDebug`
- macOS/Linux: `bash ./gradlew assembleDebug`

## Escolher como tela inicial

1. Instale e abra o Lume Launcher.
2. Toque no ícone de configurações no topo.
3. Escolha **Abrir configurações**.
4. Nas configurações de aplicativos padrão do Android, selecione **Lume Launcher** como aplicativo de início/tela inicial.

O caminho exato dos menus pode variar conforme o fabricante do aparelho.

## Estrutura do projeto

```text
LumeLauncher/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/dev/lume/launcher/
│       │   ├── MainActivity.kt
│       │   ├── data/
│       │   │   ├── FavoritesRepository.kt
│       │   │   ├── InstalledApp.kt
│       │   │   └── LauncherAppsRepository.kt
│       │   └── ui/
│       │       ├── LumeLauncherApp.kt
│       │       ├── components/AppTile.kt
│       │       └── theme/
│       │           ├── Colors.kt
│       │           └── LumeTheme.kt
│       └── res/
├── gradle/wrapper/gradle-wrapper.properties
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
└── settings.gradle.kts
```

## Decisões de privacidade

O manifesto declara um filtro `<queries>` para atividades `MAIN` + `LAUNCHER`, em vez da permissão ampla `QUERY_ALL_PACKAGES`. O projeto não inclui permissões de rede nem coleta de dados. A lista de favoritos é armazenada apenas nas preferências locais do aplicativo.

## Limitações desta primeira versão

- Ainda não hospeda widgets Android reais na área de trabalho.
- Ainda não tem páginas múltiplas, pastas, gestos configuráveis, pacotes de ícones ou backup/exportação.
- A grade apresenta atividades de inicialização disponibilizadas pelo Android para o perfil atual.
- A compilação e a instalação precisam ser verificadas em um ambiente com JDK 17, Android SDK 36 e acesso à internet.

## Próximas etapas naturais

1. Adicionar dock personalizável e páginas da tela inicial.
2. Implementar widgets reais com `AppWidgetHost` e `AppWidgetManager`.
3. Criar configurações de grade, tamanho dos ícones e papel de parede.
4. Adicionar pastas, arrastar/soltar e backup das preferências.
