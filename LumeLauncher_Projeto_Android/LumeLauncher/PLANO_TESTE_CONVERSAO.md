# Plano de teste de conversão — Lume Launcher

Este checklist organiza a validação do Lume Launcher após uma conversão, migração ou alteração do projeto. O foco é confirmar que o APK continua compilando e que as funções já implementadas permanecem corretas. Marque cada item somente depois de executar o teste e registrar o resultado.

## Escopo conhecido

Funcionalidades cobertas: seleção como tela inicial, listagem e abertura de aplicativos iniciáveis, busca por nome, favoritos persistidos, atualização da lista, relógio/data e tema escuro. O projeto declara `minSdk 26`, `targetSdk 36`, usa Kotlin + Jetpack Compose, JDK 17 e pacote `dev.lume.launcher`.

Widgets reais, dock personalizável, pastas, múltiplas páginas, backup/exportação e gestos configuráveis não fazem parte desta validação: ainda não estão implementados.

## 1. Preparação

- [ ] Registrar o commit/versão testada e o que foi convertido ou alterado.
- [ ] Preparar um dispositivo ou emulador Android 8.0 (API 26) ou superior; incluir uma versão recente do Android.
- [ ] Confirmar JDK 17 e Android SDK Platform 36 disponíveis.
- [ ] Anotar fabricante, modelo, versão/API do Android e tamanho/densidade da tela.
- [ ] Instalar a versão anterior do app, se houver, e registrar os favoritos antes de atualizar.

## 2. Compilação e geração do APK

Na raiz do projeto Gradle (`LumeLauncher_Projeto_Android/LumeLauncher`):

```bash
java -version
bash ./gradlew clean assembleDebug
```

- [ ] A compilação termina sem erros.
- [ ] O APK existe em `app/build/outputs/apk/debug/app-debug.apk`.
- [ ] A execução do workflow **Build Android APK** termina com sucesso e publica o artefato `lume-launcher-debug-apk`.
- [ ] (Windows) `gradlew.bat assembleDebug` também funciona, se Windows fizer parte do ambiente de desenvolvimento.

**Resultado esperado:** APK de debug gerado, instalável e sem falha de build.

## 3. Instalação e seleção como tela inicial

- [ ] Instalar o APK em dispositivo/emulador compatível.
- [ ] Abrir o Lume Launcher pela lista de aplicativos.
- [ ] Confirmar que aparece como opção de aplicativo de tela inicial nas configurações do Android.
- [ ] Selecioná-lo como tela inicial e pressionar o botão Home.
- [ ] Reiniciar o dispositivo e confirmar que o Android mantém a tela inicial selecionada.
- [ ] Abrir as configurações do Android pelo atalho da interface e confirmar que a tela apropriada é exibida.

**Resultado esperado:** o sistema reconhece o Lume Launcher como launcher e consegue iniciá-lo pela ação Home.

## 4. Aplicativos, busca e atualização

- [ ] Confirmar que a tela lista aplicativos iniciáveis disponíveis no perfil atual.
- [ ] Tocar em um ícone e confirmar que o aplicativo correspondente abre.
- [ ] Digitar parte do nome na busca e conferir que os resultados correspondem ao texto.
- [ ] Pesquisar um nome sem correspondência e verificar o estado vazio, sem travamento.
- [ ] Limpar a busca e verificar que a lista completa retorna.
- [ ] Instalar ou remover um app de teste, voltar à tela inicial e conferir a atualização da lista.
- [ ] Acionar o botão de atualizar, se disponível, e confirmar que a lista é recarregada.
- [ ] Testar nomes de apps longos, acentuados e com caracteres especiais.

**Resultado esperado:** lista, pesquisa, abertura e atualização funcionam sem crash ou resultados incoerentes.

## 5. Favoritos e persistência

- [ ] Adicionar um aplicativo aos favoritos com toque longo.
- [ ] Confirmar que a ação de remover favorito também funciona.
- [ ] Fechar e reabrir o launcher; verificar que os favoritos foram mantidos.
- [ ] Reiniciar o dispositivo e verificar novamente a persistência.
- [ ] Atualizar o APK sobre a instalação existente e confirmar que favoritos anteriores continuam disponíveis.
- [ ] Desinstalar e reinstalar apenas se necessário, registrando que a desinstalação pode apagar dados locais.

**Resultado esperado:** favoritos são adicionados/removidos corretamente e permanecem salvos após reabertura e reinicialização.

## 6. Interface e compatibilidade

- [ ] Verificar relógio, data, tema escuro, ícones, textos e cores.
- [ ] Conferir que a interface não corta nem sobrepõe conteúdo em telas pequenas e grandes.
- [ ] Testar orientação retrato e paisagem, se suportadas pelo dispositivo.
- [ ] Conferir comportamento com fonte/tamanho de tela ampliados.
- [ ] Repetir os fluxos principais em pelo menos uma API próxima ao mínimo suportado e uma API recente.
- [ ] Confirmar que voltar à tela inicial não deixa a interface em estado quebrado.

**Resultado esperado:** conteúdo legível, layout utilizável e funções principais consistentes nas configurações testadas.

## 7. Privacidade e permissões

- [ ] Conferir no manifesto que a descoberta de apps usa as consultas declaradas para atividades `MAIN` + `LAUNCHER`, sem adicionar `QUERY_ALL_PACKAGES` sem justificativa.
- [ ] Verificar que não foram adicionadas permissões não necessárias, como contatos, localização ou acessibilidade.
- [ ] Confirmar que não aparece solicitação inesperada de permissão durante o uso dos fluxos cobertos.

**Resultado esperado:** a conversão não amplia desnecessariamente as permissões nem altera as decisões de privacidade documentadas.

## 8. Regressão e estabilidade

- [ ] Percorrer novamente: selecionar launcher → visualizar apps → pesquisar → abrir app → voltar à Home.
- [ ] Usar o launcher por alguns minutos e alternar entre aplicativos sem crash ou tela em branco.
- [ ] Verificar logs de crash/erros durante os testes, quando houver acesso ao Logcat.
- [ ] Confirmar que o launcher volta a responder após fechar e reabrir a atividade.

**Resultado esperado:** nenhum defeito crítico nos fluxos existentes e nenhum crash reproduzível.

## 9. Registro dos resultados

Para cada falha, registrar:

- Dispositivo/emulador, fabricante e versão/API do Android.
- Versão/commit do APK e passos para reproduzir.
- Resultado esperado e resultado observado.
- Evidência (captura de tela ou trecho relevante do Logcat), sem incluir dados pessoais.
- Severidade: **bloqueador**, **alta**, **média** ou **baixa**.

## Critérios de aprovação

- [ ] Build e instalação concluídos.
- [ ] O sistema permite selecionar o app como tela inicial.
- [ ] Listagem, busca, abertura de apps e favoritos funcionam.
- [ ] Favoritos sobrevivem à reabertura e à reinicialização.
- [ ] Não há regressão crítica ou crash reproduzível nos fluxos principais.
- [ ] Permissões e consultas de apps permanecem dentro do escopo de privacidade do projeto.

> Este documento é um plano de execução. Os itens não foram considerados aprovados apenas por terem sido adicionados ao checklist; registre os resultados após rodar os testes no dispositivo/emulador.
