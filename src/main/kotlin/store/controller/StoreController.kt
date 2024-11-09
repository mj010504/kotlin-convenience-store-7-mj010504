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
import store.view.InputView.askToPurchaseWithoutPromotion
import store.view.InputView.getPurchase
import store.view.InputView.getProducts
import store.view.InputView.getPromotions
import store.view.OutputView.printInventory


class StoreController {

    fun run() {
        val promotions = getPromotions()
        val products = getProducts(promotions)

        val inventory = Inventory(products)
        printInventory(inventory)

        val purchaseItems = getPurchaseItem(inventory)
        val promotionedPurchases = checkPromotion(inventory, purchaseItems)
        promotionedPurchases.forEach { purchase ->
            println(purchase)
        }

    }

    private fun getPurchaseItem(inventory: Inventory): List<PurchaseItem> {
        try {
            return getPurchase(inventory)
        } catch (e: Exception) {
            println(e.message)
            return getPurchase(inventory)
        }

    }

    private fun checkPromotion(inventory: Inventory, purchaseItems: List<PurchaseItem>) : List<PromotionedPurchase> {
        val appliedPromotions : MutableList<PromotionedPurchase> = mutableListOf()
        purchaseItems.forEach { item ->
            when (item.promotionStatus) {
                NONE, PROMOTION_APPLIED -> {}
                OUT_OF_STOCK -> confirmPurcahseWithoutPromotion(inventory, item)?.let { promotion ->
                    appliedPromotions.add(promotion)
                }
                NOT_APPLIED -> confirmApplyPromotion(inventory, item)
            }
        }

        return appliedPromotions
    }

    private fun confirmPurcahseWithoutPromotion(inventory: Inventory, purchaseItem: PurchaseItem) : PromotionedPurchase? {
        if(!askToPurchaseWithoutPromotion(purchaseItem.productName, inventory.getNonPromotionalProductCount(purchaseItem))) {
            askToAnotherPurchase(inventory)
            return null
        }
        return PromotionedPurchase(purchaseItem, inventory.getBonusItemCount(purchaseItem), inventory.getPromotionalPrice(purchaseItem))

    }

    private fun confirmApplyPromotion(inventory: Inventory, purchaseItem: PurchaseItem) : PromotionedPurchase? {
        val additionalProductCount = inventory.getAdditionalProductCount(purchaseItem.productName)
        if(askToApplyPromotion(purchaseItem.productName, additionalProductCount)) {
            purchaseItem.addPromotionedProduct(additionalProductCount)
            return PromotionedPurchase(purchaseItem, inventory.getBonusItemCount(purchaseItem), inventory.getPromotionalPrice(purchaseItem))
        }
        return null
    }

    private fun askToAnotherPurchase(inventory: Inventory) {
        if(askToAnotherPurchase()) getPurchaseItem(inventory)
    }


}