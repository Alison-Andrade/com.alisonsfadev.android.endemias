# SISVETOR - mobile

Aplicativo Android para apoiar agentes de combate às endemias no registro de visitas e tratamentos em imóveis.

## Funcionalidades

- Listagem de quarteirões e imóveis, com indicação de visitas concluídas.
- Ficha de visita com situações **Trabalhado**, **Fechado** e **Recusado**.
- Registro do horário de início ao selecionar **Trabalhado**.
- Registro de depósitos eliminados, depósitos tratados e quantidade de larvicida, com controles de quantidade editáveis.
- Salvamento da visita e opção de avançar para o próximo imóvel do quarteirão.
- Temas claro e escuro.

O imóvel é considerado tratado quando há depósitos eliminados ou depósitos tratados com larvicida.

## Tecnologias

Kotlin, Jetpack Compose, Material 3, Navigation Compose, Hilt, Coroutines e StateFlow. A organização do código separa interface, domínio e dados.

## Como executar

1. Abra o projeto no Android Studio.
2. Instale o SDK solicitado pelo projeto e sincronize o Gradle. A configuração do Gradle utiliza JDK 25.
3. Execute o módulo `app` em um emulador ou dispositivo com Android 7.0 (API 24) ou superior.

O login é demonstrativo: basta preencher o usuário e a senha com qualquer valor.

Para gerar o APK de debug:

```bash
./gradlew :app:assembleDebug
```

O APK fica em `app/build/outputs/apk/debug/app-debug.apk`.

## Estado atual

O projeto está em desenvolvimento. Os imóveis são dados de exemplo e as visitas são salvas apenas em memória, sendo perdidas ao encerrar o processo. Ainda não há autenticação real, banco de dados persistente ou sincronização com servidor. O resumo inicial usa dados demonstrativos; as telas de relatórios e perfil ainda são básicas.

## Verificação

```bash
./gradlew :app:testDebugUnitTest :app:lintDebug
./gradlew :app:connectedDebugAndroidTest
```

Os testes de interface precisam de um emulador ou dispositivo conectado.
