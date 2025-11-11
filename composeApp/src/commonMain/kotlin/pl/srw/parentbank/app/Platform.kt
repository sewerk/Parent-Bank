package pl.srw.parentbank.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform