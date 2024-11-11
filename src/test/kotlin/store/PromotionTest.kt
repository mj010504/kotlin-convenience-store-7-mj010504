package store

import camp.nextstep.edu.missionutils.test.Assertions.assertNowTest
import camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest
import camp.nextstep.edu.missionutils.test.NsTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import store.model.Inventory
import store.model.PromotionStatus
import store.model.PromotionedPurchase
import store.model.PurchaseItem
import store.view.InputView.getProducts
import store.view.InputView.getPromotions
import store.view.OutputView
import java.time.LocalDate

class PromotionTest : NsTest() {

    @Test
    fun `기한이 지난 프로모션은 적용되지 않는다`() {
        assertNowTest({
            run("[오렌지주스-2]", "N", "N")
            assertThat(output().replace("\\s".toRegex(), "")).contains("내실돈3,600")
        }, LocalDate.of(2023, 11, 20).atStartOfDay())
    }

    @Test
    fun `프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우, 혜택에 대한 안내 메시지를 출력한다`() {
        assertSimpleTest {
            run("[콜라-2]", "Y", "N", "N")
            assertThat(output().contains("현재 콜라은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니끼? (Y/N)"))
        }
    }

    @Test
    fun `프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우, 사용자가 Y를 입력하면 프로모션이 적용된다`() {
        assertSimpleTest {
            run("[콜라-2]", "Y", "N", "N")
            assertThat(output().contains("총구매액_3_3,000"))
            assertThat(output().contains("행사할인_-1,000"))
            assertThat(output().contains("내실돈_2,000"))
        }
    }

    @Test
    fun `프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우, 사용자가 N을 입력하면 프로모션이 적용되지 않는다`() {
        assertSimpleTest {
            run("[콜라-2]", "N", "N", "N")
            assertThat(output().contains("총구매액_2_2,000"))
            assertThat(output().contains("행사할인_-0"))
            assertThat(output().contains("내실돈_2,000"))
        }
    }

    @Test
    fun `프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부에 대한 안내 메시지를 출력한다`() {
        assertSimpleTest {
            run("[탄산수-5]", "N", "N")
            assertThat(output().contains("현재 탄산수 2개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"))
        }
    }

    @Test
    fun `프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 사용자가 Y를 입력하면 일부 수량은 프로모션이 적용되지 않은 상태로 구매한다`() {
        assertSimpleTest {
            run("[탄산수-5]", "Y", "N", "N")
            assertThat(output().contains("총구매액_5_6000"))
            assertThat(output().contains("행사할인_-1200"))
            assertThat(output().contains("내실돈_4800"))
        }
    }

    @Test
    fun `프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 사용자가 N을 입력하여 구매가 취소되면 다른 상품 구매 의사를 물어본다`() {
        assertSimpleTest {
            run("[탄산수-5]", "N", "N")
            assertThat(output().contains("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)"))
        }
    }

    override fun runMain() {
        main()
    }
}