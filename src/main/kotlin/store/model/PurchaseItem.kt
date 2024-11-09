package store.model

class PurchaseItem(
    val productName: String,
    var quantity: Int,
    val price : Int,
    val promotionStatus: PromotionStatus
) {
    fun addPromotionedProduct(additionalProductCount : Int) {
        quantity += additionalProductCount
    }
}
