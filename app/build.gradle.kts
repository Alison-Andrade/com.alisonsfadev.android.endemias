plugins {
    // Configura este módulo como um aplicativo Android.
    alias(libs.plugins.android.application)
    // Habilita o compilador do Jetpack Compose para Kotlin.
    alias(libs.plugins.kotlin.compose)
    // Integra a injeção de dependências do Hilt ao Android.
    alias(libs.plugins.hilt.android)
    // Processa anotações Kotlin usadas pelo compilador do Hilt.
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.alisonsfadev.endemias"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.alisonsfadev.endemias"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Adiciona extensões Kotlin para APIs básicas do Android.
    implementation(libs.androidx.core.ktx)
    // Fornece suporte a corrotinas nos componentes de ciclo de vida.
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Disponibiliza dispatchers de corrotinas para a thread principal do Android.
    implementation(libs.kotlinx.coroutines.android)

    // Alinha as versões das bibliotecas do Jetpack Compose.
    implementation(platform(libs.androidx.compose.bom))
    // Integra o Jetpack Compose às atividades Android.
    implementation(libs.androidx.activity.compose)
    // Fornece componentes visuais do Material Design 3.
    implementation(libs.androidx.compose.material3)
    // Fornece os elementos fundamentais da interface Compose.
    implementation(libs.androidx.compose.ui)
    // Oferece recursos gráficos para a interface Compose.
    implementation(libs.androidx.compose.ui.graphics)
    // Habilita prévias das telas Compose no Android Studio.
    implementation(libs.androidx.compose.ui.tooling.preview)
    // Disponibiliza o conjunto estendido de ícones Material.
    implementation(libs.androidx.compose.material.icons.extended)
    // Permite navegar entre telas criadas com Compose.
    implementation(libs.androidx.navigation.compose)

    // Define a API HTTP usada para realizar requisições de rede.
    implementation(libs.retrofit)
    // Converte respostas JSON do Retrofit em objetos Kotlin.
    implementation(libs.converter.gson)
    // Executa as chamadas HTTP e permite configurar o cliente de rede.
    implementation(libs.okhttp)
    // Registra requisições e respostas HTTP para diagnóstico.
    implementation(libs.logging.interceptor)

    // Disponibiliza a injeção de dependências do Hilt no aplicativo.
    implementation(libs.hilt.android)
    // Gera o código de injeção de dependências do Hilt.
    ksp(libs.hilt.android.compiler)
    // Integra o Hilt aos destinos de navegação Compose.
    implementation(libs.hilt.navigation.compose)

    // Executa testes unitários com JUnit 4.
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    // Alinha as versões do Compose usadas nos testes instrumentados.
    androidTestImplementation(platform(libs.androidx.compose.bom))
    // Testa componentes Compose com JUnit 4 em dispositivos Android.
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    // Automatiza interações com a interface Android em testes instrumentados.
    androidTestImplementation(libs.androidx.espresso.core)
    // Fornece integração do JUnit com testes instrumentados Android.
    androidTestImplementation(libs.androidx.junit)

    // Inclui o manifesto necessário para executar testes Compose no modo debug.
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    // Habilita ferramentas de inspeção da interface Compose no modo debug.
    debugImplementation(libs.androidx.compose.ui.tooling)

    coreLibraryDesugaring(libs.desugar.jdk.libs)
}
