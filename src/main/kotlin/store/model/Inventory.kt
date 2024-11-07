package store.model

import store.utils.ErrorHandler.getErrorMessage

class Inventory(val products: List<Product>) {
    fun containsProductName(productName: String) {
        require(products.any { it.name == productName }) { getErrorMessage(INVALID_PRODUCT_NAME.format(productName)) }
    }

    fun checkQuantity(productName: String, quantity: Int) {
        val productsByName = products.filter { it.name == productName }
        val totalQuantity = productsByName.sumOf { it.quantity }
        if(totalQuantity < quantity) throw IllegalArgumentException(getErrorMessage(INSUFFICIENT_QUANTITY))
    }

    fun getAdditionalProductCount(productName: String): Int {
        val products = products.filter { it.name == productName }
        val productWithPromotion = products.find { it.promotion != null }
        return productWithPromotion!!.promotion!!.getCount
    }

    fun getNonPromotionalProductCount(productName: String, quantity: Int): Int {
        val products = products.filter { it.name == productName }
        val productWithPromotion = products.find { it.promotion != null }
        val totalPromotionCount =
            productWithPromotion!!.promotion!!.buyCount + productWithPromotion.promotion!!.getCount
        val promotionalProductCount =
            totalPromotionCount * (quantity / totalPromotionCount)
        return quantity - promotionalProductCount
    }

    companion object {
        private const val INVALID_PRODUCT_NAME = "%s는(은) 존재하지 않는 상품입니다."
        private const val INSUFFICIENT_QUANTITY = "재고 수량을 초과하여 구매할 수 없습니다."
    }

}