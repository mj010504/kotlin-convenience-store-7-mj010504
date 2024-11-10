package store

import camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest
import camp.nextstep.edu.missionutils.test.NsTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource

class PurchaseInputTest : NsTest() {

    @ParameterizedTest
    @ValueSource(strings = ["[콜라3]","콜라-3", "(콜라-3)", "[콜라-열개]"])
    fun `구매 형식 입력을 지키지 않으면 예외가 발생한다`(purchaseInput : String) {
        assertSimpleTest {
            runException(purchaseInput)
            assertThat(output()).contains("[ERROR] 올바르지 않은 구매 형식입니다. 다시 입력해 주세요.")
        }
    }

    @ParameterizedTest
    @MethodSource("provideInvalidProduct")
    fun `존재하지 않는 상품을 입력한 경우 예외가 발생한다`(productName : String) {
        assertSimpleTest {
            runException("[$productName-3]")
            assertThat(output()).contains("[ERROR] ${productName}는(은) 존재하지 않는 상품입니다. 다시 입력해 주세요.")
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["[콜라-21]","[사이다-16]", "[에너지바-10]", "[감자칩-12]"])
    fun `구매 수량이 재고 수량을 초과한 경우 예외가 발생한다`(purchaseInput : String) {
        assertSimpleTest {
            runException(purchaseInput)
            assertThat(output()).contains("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.")
        }
    }

    companion object {
        @JvmStatic
        fun provideInvalidProduct(): List<String> {
            return listOf(
                "비타민",
                "두유",
                "제로콜라",
                "요플레"
            )
        }
    }

    override fun runMain() {
        main()
    }
}