package osornogourmet.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import osornogourmet.config.JwtConfig
import osornogourmet.domain.model.User
import java.util.*

class JwtTokenService {
    fun generateToken(user: User): String {
        return JWT.create()
            .withAudience(JwtConfig.audience)
            .withIssuer(JwtConfig.issuer)
            .withClaim("email", user.email)
            .withClaim("userId", user.id)
            .withExpiresAt(Date(System.currentTimeMillis() + JwtConfig.expirationTime))
            .sign(Algorithm.HMAC256(JwtConfig.secret))
    }
}
