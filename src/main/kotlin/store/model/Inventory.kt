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

    private fun findProductWithPromotion(productName: String): Product? {
        return products.find { it.name == productName && it.promotion != null }
    }

    private fun findProductWithoutPromotion(productName : String) : Product? {
        return products.find { it.name == productName && it.promotion == null }
    }

    private fun getTotalPromotionCount(product: Product): Int {
        val promotion = product.promotion!!
        return promotion.buyCount + promotion.getCount
    }

    fun getPriceByName(productName: String) : Int {
        return products.find { it.name == productName }!!.price
    }

    fun getAdditionalProductCount(productName: String): Int {
        val product = findProductWithPromotion(productName)
        return product!!.promotion!!.getCount
    }

    fun getPromotionalProductCount(purchaseItem: PurchaseItem): Int {
        val product = findProductWithPromotion(purchaseItem.productName) ?: return 0
        val totalPromotionCount = getTotalPromotionCount(product)

        return if (purchaseItem.quantity <= product.quantity)
            totalPromotionCount * (purchaseItem.quantity / totalPromotionCount)
        else
            totalPromotionCount * (product.quantity / totalPromotionCount)
    }

    fun getNonPromotionalProductCount(purchaseItem: PurchaseItem): Int = purchaseItem.quantity - getPromotionalProductCount(purchaseItem)

    fun getPromotionalPrice(purchaseItem: PurchaseItem) : Int {
        val product = findProductWithPromotion(purchaseItem.productName)
        val totalPromotionCount = getTotalPromotionCount(product!!)
        val promotionalProductCount = getPromotionalProductCount(purchaseItem)
        return promotionalProductCount / totalPromotionCount * product.price
    }

    fun getBonusItemCount(purchaseItem: PurchaseItem) : Int {
        val promotionalCount = getPromotionalProductCount(purchaseItem)
        val product = findProductWithPromotion(purchaseItem.productName)
        val totalPromotionCount = getTotalPromotionCount(product!!)
        return promotionalCount / totalPromotionCount
    }

    fun purchaseItems(purchases: List<PromotionedPurchase>) {
        purchases.forEach { purchase ->
            val productWithPromotion = findProductWithPromotion(purchase.purchaseItem.productName)
            val productWithoutPromotion = findProductWithoutPromotion(purchase.purchaseItem.productName)
            var quantity = purchase.purchaseItem.quantity
                productWithPromotion?.let { product ->
                    while(product.quantity > 0 && quantity > 0) {
                        product.quantity -= 1
                        quantity -= 1
                    }
                }
                productWithoutPromotion?.let { product ->
                    while(productWithoutPromotion.quantity > 0 && quantity > 0) {
                        product.quantity -= 1
                        quantity -= 1
                    }
                }

            }
    }

    companion object {
        private const val INVALID_PRODUCT_NAME = "%s는(은) 존재하지 않는 상품입니다."
        private const val INSUFFICIENT_QUANTITY = "재고 수량을 초과하여 구매할 수 없습니다."
    }

}