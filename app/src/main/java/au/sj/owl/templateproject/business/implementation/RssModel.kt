package au.sj.owl.templateproject.business.implementation

class RssModel {
    companion object {
        /**
         * extract img ulr kind of "img src="url/to/img""
         * @return url of img (in this case "url/to/img" without "\"")
         */
        fun getImgUrlFromDescription(inp: String): String {
            var regSrc = Regex("img src=\"\\S*\"")
            var regUrl = Regex("\"\\S*\"")
            val sSrc = regSrc.find(inp)?.value
            var sUrl: String? = if (sSrc != null) regUrl.find(sSrc)?.value else ""
            if (sUrl == null) sUrl = ""
            else sUrl = sUrl.replace("\"", "")
            return sUrl
        }
    }

}