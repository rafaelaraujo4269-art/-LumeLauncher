# Lume Launcher

Launcher Android independente desenvolvido em Kotlin + Jetpack Compose.

## Código-fonte

O projeto Android está em:

`LumeLauncher_Projeto_Android/LumeLauncher/`

A pasta que contém `settings.gradle.kts` é a raiz do projeto Gradle.

## Compilar pelo GitHub Codespaces

Abra o repositório em um Codespace e execute no terminal:

```bash
cd LumeLauncher_Projeto_Android/LumeLauncher
bash ./gradlew assembleDebug
```

O script `gradlew` deste projeto baixa a distribuição oficial Gradle 8.13 para o cache do usuário. Não é necessário instalar Android Studio, mas é necessário ter JDK 17, Android SDK Platform 36 e conexão com a internet para baixar as ferramentas e dependências.

## Gerar APK automaticamente

O workflow **Build Android APK** em `.github/workflows/build-android.yml`:

- é executado quando há push na branch `main`;
- também pode ser iniciado manualmente em **Actions → Build Android APK → Run workflow**;
- prepara JDK 17 e Android SDK 36;
- executa `clean assembleDebug`;
- publica o APK como artefato chamado `lume-launcher-debug-apk`, disponível por 14 dias.

Para baixar o APK, abra a execução concluída na aba **Actions** e procure a seção **Artifacts**.

## Configuração Android

| Propriedade | Valor |
|---|---:|
| Linguagem | Kotlin |
| Interface | Jetpack Compose |
| minSdk | 26 (Android 8.0) |
| compileSdk | 36 |
| targetSdk | 36 |
| JDK | 17 |
| Gradle | 8.13 |
| Android Gradle Plugin | 8.11.1 |
| Kotlin | 2.2.21 |

O pacote Android é `dev.lume.launcher`.

## Funcionalidades da primeira versão

- Pode ser selecionado como aplicativo de tela inicial.
- Lista e abre aplicativos iniciáveis do perfil atual.
- Busca por nome.
- Favoritos persistidos localmente.
- Relógio, data e tema visual próprio.

Widgets Android reais, dock personalizável, pastas e páginas múltiplas ainda estão no planejamento.
