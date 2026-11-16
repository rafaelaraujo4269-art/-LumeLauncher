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

- Android Studio com suporte ao Android Gradle Plugin 8.11.1.
- JDK 17.
- Android SDK Platform 36 instalado pelo SDK Manager.
- Conexão com a internet na primeira sincronização para baixar Gradle e dependências.

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

O conjunto Kotlin/Gradle/AGP foi escolhido conforme a tabela de compatibilidade oficial do Kotlin. O Compose BOM segue a configuração oficial atual documentada pelo Android Developers.

## Abrir no Android Studio

1. Extraia o ZIP.
2. No Android Studio, escolha **Open** e selecione a pasta `LumeLauncher` (a que contém `settings.gradle.kts`).
3. Aguarde a sincronização do Gradle. Na primeira vez, o script `gradlew` baixa a distribuição oficial Gradle 8.13 para o cache local.
4. Se o Android Studio pedir, instale **Android SDK Platform 36** pelo SDK Manager.
5. Execute em um aparelho ou emulador com Android 8.0 ou superior.

Também é possível compilar pelo terminal:

- Windows: `gradlew.bat assembleDebug`
- macOS/Linux: `./gradlew assembleDebug`

APK de depuração esperado: `app/build/outputs/apk/debug/app-debug.apk`.

## Escolher como tela inicial

1. Instale e abra o Lume Launcher.
2. Toque no ícone de configurações no topo.
3. Escolha **Abrir configurações**.
4. Nas configurações de aplicativos padrão do Android, selecione **Lume Launcher** como aplicativo de início/tela inicial.

O caminho exato dos menus pode variar conforme o fabricante do aparelho. Também é possível escolher o launcher ao pressionar o botão Início, dependendo da versão do Android.

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
- A compilação e a instalação precisam ser verificadas no Android Studio com SDK e dependências baixados; este ambiente de geração não possui Android SDK/Gradle instalado para executar o build aqui.

## Próximas etapas naturais

1. Adicionar dock personalizável e páginas da tela inicial.
2. Implementar widgets reais com `AppWidgetHost` e `AppWidgetManager`.
3. Criar configurações de grade, tamanho dos ícones e papel de parede.
4. Adicionar pastas, arrastar/soltar e backup das preferências.
