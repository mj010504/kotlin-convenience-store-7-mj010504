package store.model

import camp.nextstep.edu.missionutils.DateTimes.now

enum class PromotionStatus {
    NONE, // 프로모션이 원래 없는 상품
    OUT_OF_STOCK, // 프로모션 상품의 재고가 부족한 상품
    PROMOTION_APPLIED, // 프로모션 상품의 재고가 충분하고 구매한 수량이 딱 맞는 상품
    NOT_APPLIED; // 프로모션 상품의 재고가 충분하지만 구매한 수량이 적은 상품

    companion object {
        fun convertToStatus(inventory: Inventory, productName: String, quantity: Int): PromotionStatus {
            val products = inventory.products.filter { it.name == productName }
            if (products.size != 2) return NONE
            val productWithPromotion = products.find { it.promotion != null }
            val promotion = productWithPromotion!!.promotion!!
            val totalPromotionCount = promotion.buyCount + promotion.getCount
            if (!isValidDate(promotion)) return NONE
            if (productWithPromotion.quantity >= quantity && quantity % totalPromotionCount == 0) return PROMOTION_APPLIED
            val promotionalProductCount = totalPromotionCount * (quantity / totalPromotionCount)
            val NonPromotionalProductCount = quantity - promotionalProductCount
            if (NonPromotionalProductCount == promotion.buyCount && (productWithPromotion.quantity - promotionalProductCount) >= totalPromotionCount) return NOT_APPLIED
            return OUT_OF_STOCK
        }

        private fun isValidDate(promotion: Promotion): Boolean {
            val startDate = promotion.startDate
            val endDate = promotion.endDate
            if (now().toLocalDate() in startDate..endDate) return true
            return false
        }
    }

}