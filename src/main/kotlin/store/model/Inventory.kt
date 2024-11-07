package store.model

import store.utils.ErrorHandler.getErrorMessage

class Inventory(val products: List<Product>) {
    fun containsProductName(productName: String) {
        require(products.any { it.name == productName }) { getErrorMessage(INVALID_PRODUCT_NAME.format(productName)) }
    }

    fun checkQuantity(productName: String, quantity: Int) {
        val productsByName = products.filter { it.name == productName }
        val totalQuantity = productsByName.sumOf { it.quantity }
        require(totalQuantity >= quantity) { getErrorMessage(INSUFFICIENT_QUANTITY) }
    }

    companion object {
        private const val INVALID_PRODUCT_NAME = "%s는 존재하지 않는 상품입니다."
        private const val INSUFFICIENT_QUANTITY = "재고 수량을 초과하여 구매할 수 없습니다."
    }

}