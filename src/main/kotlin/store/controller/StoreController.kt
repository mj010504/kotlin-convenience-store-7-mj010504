package store.controller

import store.model.Inventory
import store.model.PromotionStatus.NONE
import store.model.PromotionStatus.OUT_OF_STOCK
import store.model.PromotionStatus.PROMOTION_APPLIED
import store.model.PromotionStatus.NOT_APPLIED
import store.model.PromotionedPurchase
import store.model.PurchaseItem
import store.view.InputView.askToAnotherPurchase
import store.view.InputView.askToApplyPromotion
import store.view.InputView.askToMembershipApply
import store.view.InputView.askToPurchaseWithoutPromotion
import store.view.InputView.getPurchase
import store.view.InputView.getProducts
import store.view.InputView.getPromotions
import store.view.OutputView.printInventory
import store.view.OutputView.printReceipt

class StoreController {

    fun run() {
        val promotions = getPromotions()
        val products = getProducts(promotions)
        val inventory = Inventory(products)
        purchase(inventory)
    }

    private fun purchase(inventory: Inventory) {
        val purchaseItems = getPurchaseItem(inventory)
        val promotionedPurchases = checkPromotion(inventory, purchaseItems)
        if(promotionedPurchases.size != 0) purchaseItems(inventory, promotionedPurchases, confirmApplyMembershipDiscount())
    }


    private fun getPurchaseItem(inventory: Inventory): List<PurchaseItem> {
        try {
            printInventory(inventory)
            return getPurchase(inventory)
        } catch (e: Exception) {
            println(e.message)
            return getPurchase(inventory)
        }

    }

    private fun checkPromotion(inventory: Inventory, purchaseItems: List<PurchaseItem>) : List<PromotionedPurchase> {
        val promotionedPurchases : MutableList<PromotionedPurchase> = mutableListOf()
        purchaseItems.forEach { item ->
            when (item.promotionStatus) {
                NONE -> { promotionedPurchases.add(PromotionedPurchase(item, 0, 0)) }
                PROMOTION_APPLIED -> { promotionedPurchases.add(PromotionedPurchase(item, inventory.getBonusItemCount(item), inventory.getPromotionalPrice(item))) }
                OUT_OF_STOCK -> confirmPurcahseWithoutPromotion(inventory, item)?.let { purchase ->
                    promotionedPurchases.add(purchase)
                }
                NOT_APPLIED -> promotionedPurchases.add(confirmApplyPromotion(inventory, item))
            }
        }

        return promotionedPurchases
    }

    private fun confirmPurcahseWithoutPromotion(inventory: Inventory, purchaseItem: PurchaseItem) : PromotionedPurchase? {
        if(!askToPurchaseWithoutPromotion(purchaseItem.productName, inventory.getNonPromotionalProductCount(purchaseItem))) {
            askToAnotherPurchase(inventory)
            return null
        }
        return PromotionedPurchase(purchaseItem, inventory.getBonusItemCount(purchaseItem), inventory.getPromotionalPrice(purchaseItem))

    }

    private fun confirmApplyPromotion(inventory: Inventory, purchaseItem: PurchaseItem) : PromotionedPurchase {
        val additionalProductCount = inventory.getAdditionalProductCount(purchaseItem.productName)
        if(askToApplyPromotion(purchaseItem.productName, additionalProductCount)) {
            purchaseItem.addPromotionedProduct(additionalProductCount)
            return PromotionedPurchase(purchaseItem, inventory.getBonusItemCount(purchaseItem), inventory.getPromotionalPrice(purchaseItem))
        }
        return PromotionedPurchase(purchaseItem, inventory.getBonusItemCount(purchaseItem), inventory.getPromotionalPrice(purchaseItem))
    }

    private fun confirmApplyMembershipDiscount() : Boolean {
        if(askToMembershipApply()) return true
        return false
    }

    private fun purchaseItems(inventory: Inventory, purchases: List<PromotionedPurchase>, isMemeberShip : Boolean) {
        inventory.purchaseItems(purchases)
        val totalPromotionedPrice = purchases.sumOf { purchase ->
            inventory.getPromotionalProductCount(purchase.purchaseItem) * purchase.purchaseItem.price
        }
        printReceipt(purchases, totalPromotionedPrice)
        askToAnotherPurchase(inventory)
    }

    private fun askToAnotherPurchase(inventory: Inventory) {
        if(askToAnotherPurchase()) purchase(inventory)
    }

}