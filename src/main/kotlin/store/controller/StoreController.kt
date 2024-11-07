package store.controller

import store.model.Inventory
import store.model.PromotionStatus
import store.model.PromotionStatus.NONE
import store.model.PromotionStatus.OUT_OF_STOCK
import store.model.PromotionStatus.PROMOTION_APPLIED
import store.model.PromotionStatus.NOT_APPLIED
import store.model.PurchaseItem
import store.utils.ErrorHandler.getErrorMessage
import store.view.InputView
import store.view.InputView.askToApplyPromotion
import store.view.InputView.askToPurchaseWithoutPromotion
import store.view.InputView.getPurchase
import store.view.InputView.getProducts
import store.view.InputView.getPromotions
import store.view.OutputView
import store.view.OutputView.printInventory


class StoreController {

    fun run() {
        val promotions = getPromotions()
        val products = getProducts(promotions)

        val inventory = Inventory(products)
        printInventory(inventory)

        val purchaseItems = getPurchaseItem(inventory)
        checkPromotion(inventory, purchaseItems)
    }

    private fun getPurchaseItem(inventory: Inventory): List<PurchaseItem> {
        try {
            return getPurchase(inventory)
        } catch (e: Exception) {
            println(e.message)
            return getPurchase(inventory)
        }

    }

    private fun checkPromotion(inventory: Inventory, purchaseItems: List<PurchaseItem>) {
        purchaseItems.forEach { item ->
            when (item.promotionStatus) {
                NONE, PROMOTION_APPLIED -> {}
                OUT_OF_STOCK -> confirmPurcahseWithoutPromotion(inventory, item.productName, item.quantity)
                NOT_APPLIED -> confirmApplyPromotion(inventory, item.productName)
            }
        }
    }

    private fun confirmPurcahseWithoutPromotion(inventory: Inventory, productName : String, quantity : Int) {
        if(askToPurchaseWithoutPromotion(productName, inventory.getNonPromotionalProductCount(productName, quantity))) {

        }
    }

    private fun confirmApplyPromotion(inventory: Inventory, productName: String) {
        if(askToApplyPromotion(productName, inventory.getAdditionalProductCount(productName))) {

        }
    }


}