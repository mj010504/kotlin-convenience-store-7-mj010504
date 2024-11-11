package store

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

class PurchaseTest : NsTest() {

    @Test
    fun `구매 완료시 재고가 올바르게 소진된다`() {

        // given
        val promotions = getPromotions()
        val products = getProducts(promotions)
        val inventory = Inventory(products)
        val purchases: List<PromotionedPurchase> =
            listOf(
                PromotionedPurchase(PurchaseItem("물", 10, 500, PromotionStatus.NONE), 0, 0),
                PromotionedPurchase(PurchaseItem("에너지바", 5, 1200, PromotionStatus.NONE), 0, 0)
            )

        // when
        inventory.purchaseItems(purchases)
        OutputView.printInventory(inventory)

        // then
        assertThat(output()).contains("물 500원 재고 없음", "에너지바 2,000원 재고 없음")
    }

    @Test
    fun `프로모션 기간 중이라면 프로모션 재고를 우선적으로 차감하며, 프로모션 재고가 부족할 경우에는 일반 재고를 사용한다`() {

        // given
        val promotions = getPromotions()
        val products = getProducts(promotions)
        val inventory = Inventory(products)
        val purchases: List<PromotionedPurchase> =
            listOf(
                PromotionedPurchase(
                    PurchaseItem("콜라", 11, 1000, PromotionStatus.OUT_OF_STOCK),
                    3,
                    3000
                ),
                PromotionedPurchase(
                    PurchaseItem("감자칩", 2, 1500, PromotionStatus.PROMOTION_APPLIED),
                    1,
                    1200
                )
            )

        // when
        inventory.purchaseItems(purchases)
        OutputView.printInventory(inventory)

        // then
        assertThat(output()).contains(
            "콜라 1,000원 재고 없음 탄산2+1", "콜라 1,000원 9개", "감자칩 1,500원 3개 반짝할인", "감자칩 1,500원 5개"
        )

    }

    override fun runMain() {
        main()
    }
}