package au.sj.owl.templateproject

import org.junit.*
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun rssfeed() {
        val inp = "D: jsp <p><div class=\"ec-content-image ec-thumbnail content-image-full\"><a href=\"https://www.economist.com/sites/default/files/images/print-edition/20180421_FND002_0.jpg\" class=\"ec-enlarge\"></a><img src=\"https://www.economist.com/sites/default/files/images/print-edition/20180421_FND002_0.jpg\" alt=\"\" title=\"\" width=\"1280\" height=\"720\" width=\"1280\" style=\"height: auto\" /></div></p><p>MOST governments are happy when foreigners want their bonds, especially when those foreigners are long-term holders, like central banks. But America is different. It worries that some foreign governments buy its debt to keep the dollar pricey and their own currencies cheap. This “currency manipulation” gives other countries a competitive edge, raising their own trade surpluses and America’s deficit.</p><p>Brad Setser of the Council on Foreign Relations, a think-tank, sees an “arc of intervention” across Thailand, Singapore, Taiwan and South Korea that has slowed the dollar’s decline over the past nine months. America has reportedly persuaded South Korea to forswear currency manipulation in a “side-agreement” to their revised trade deal. And on April 16th President Donald Trump tweeted that “Russia and China are playing the Currency Devaluation game...Not acceptable!”</p><p></p><p>Mr Trump’s tweet was at odds with his Treasury Department’s assessment. Every six months it must tell...<a href=\"https://www.economist.com/news/finance-and-economics/21740746-donald-trump-less-circumspect-americas-treasury-refrains-naming-any?fsrc=rss\">Continue reading</a>"
        var reg = Regex("img src=\".*\" ")
        //        var res = reg.matchEntire(inp)?.groups?.get(1)?.value
        println("jsp res = " + reg.matchEntire(inp)?.groups?.get(0)?.value)
        println("jsp res = " + reg.matchEntire(inp)?.groups?.get(1)?.value)
        assertEquals(4, 2 + 2)
    }
}
