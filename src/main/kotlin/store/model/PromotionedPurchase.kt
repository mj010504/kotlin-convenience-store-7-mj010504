package store.model

data class PromotionedPurchase(
    val purchaseItem: PurchaseItem,
    val bonusItemCount: Int,
    val promotionalPrice: Int
)