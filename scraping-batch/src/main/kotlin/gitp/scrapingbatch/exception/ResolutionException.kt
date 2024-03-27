package gitp.scrapingbatch.exception

class ResolutionException(
    message: String,
    public var rawResponseJson: String?
) : Exception(message) {
    constructor(message: String): this(message, null)
}