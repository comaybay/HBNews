class userDataList {
    var date:String ? = null
    var description:String ? = null
    var newsImage:String ? = null
    var newsSource:String ? = null
    var newsUrl:String ? = null
    var title:String ? = null
    var timestamp:String ? = null

    constructor()
    {

    }

    constructor(
        date: String?,
        description: String?,
        newsImage: String?,
        newsSource: String?,
        newsUrl: String?,
        title: String?,
        timestamp: String?
    ) {
        this.date = date
        this.description = description
        this.newsImage = newsImage
        this.newsSource = newsSource
        this.newsUrl = newsUrl
        this.title = title
        this.timestamp = timestamp
    }



}