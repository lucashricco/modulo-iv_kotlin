package br.com.igti.modulo_iv

import android.app.Application
import br.com.igti.modulo_iv.data.remote.IAlunoRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class IgtiApplication : Application()