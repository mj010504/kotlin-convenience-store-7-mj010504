package store

import camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest
import camp.nextstep.edu.missionutils.test.NsTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MemberShipTest : NsTest() {

    @Test
    fun `멤버십 할인 적용 여부를 Y라고 입력하면 30% 할인받는다`() {
        assertSimpleTest {
            run("[물-10]", "Y", "N")
            assertThat(output().replace("\\s".toRegex(), "")).contains(
                "총구매액105,000", // 총 구매액 - 수량 : 10 금액: 5,000
                "멤버십할인-1,500",
                "내실돈3,500"
            )
        }
    }

    @Test
    fun `프로모션 적용 후 남은 금액만 멤버십 할인을 적용한다`() {
        assertSimpleTest {
            run("[콜라-3],[에너지바-5]", "Y", "N")
            assertThat(output().replace("\\s".toRegex(), "")).contains(
                "총구매액813,000", // 총 구매액 - 수량 : 8 금액: 13,000
                "행사할인-1,000",
                "멤버십할인-3,000",
                "내실돈9,000"
            )
        }
    }

    @Test
    fun `멤버십 할인의 최대 한도는 8,000원 이다`() {
        assertSimpleTest {
            run("[정식도시락-8]", "Y", "N")
            assertThat(output().replace("\\s".toRegex(), "")).contains(
                "총구매액851,200", // 총 구매액 - 수량 : 8 금액: 51,200
                "행사할인-0",
                "멤버십할인-8,000", // 한도가 없다면 15,360원 멤버십 할인되어야 함
                "내실돈43,200"
            )
        }
    }

    override fun runMain() {
        main()
    }
}