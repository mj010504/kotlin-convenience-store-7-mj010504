package store

import camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest
import camp.nextstep.edu.missionutils.test.NsTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MemberShipTest : NsTest() {

    @Test
    fun `멤버십 회원은 프로모션 미적용 금액의 30%를 할인받는다`() {
        assertSimpleTest {
            run("[콜라-2]", "Y", "N", "N")
            assertThat(output().contains("총구매액_3_3,000"))
            assertThat(output().contains("행사할인_-1,000"))
            assertThat(output().contains("내실돈_2,000"))
        }
    }

    @Test
    fun `프로모션 적용 후 남은 금액만 멤버십 할인을 적용한다`() {

    }

    @Test
    fun `멤버십 할인의 최대 한도는 8,000원 이다`() {}


    override fun runMain() {
        main()
    }
}