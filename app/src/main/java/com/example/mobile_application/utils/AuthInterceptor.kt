import android.content.Context
import com.example.mobile_application.BuildConfig
import com.example.mobile_application.utils.AuthManager
import okhttp3.Interceptor
import okhttp3.Response

// Interceptor do dodawania Api-Key do wszystkich endpointów, z wyjątkiem getProfile
class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = AuthManager.getToken(context)
        val urlString = originalRequest.url.toString()

        val modifiedRequest = if (urlString.contains("/profile") || urlString.contains("/user_device")) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Api-Key ${BuildConfig.API_KEY}")
                .build()
        }

        return chain.proceed(modifiedRequest)
    }
}
