package com.example.arfinance.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.arfinance.R
import com.example.arfinance.databinding.BottomSheetSelectCategoryIconBinding
import com.example.arfinance.util.enumerian.CategoryType
import com.example.arfinance.util.interfaces.CategoryIconSelectedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CategoryBottomSheetDialog(private val listener:CategoryIconSelectedListener) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetSelectCategoryIconBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetSelectCategoryIconBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindUI()
    }

    private fun bindUI() {
        binding.apply {
            accessory.setOnClickListener {
               listener.selectIcon(CategoryType.accesory.name, R.drawable.ic_accessory)
                super.dismiss()
            }
            award.setOnClickListener {
                listener.selectIcon(CategoryType.award.name, R.drawable.ic_award)
                super.dismiss()
            }
            beer.setOnClickListener {
                listener.selectIcon(CategoryType.beer.name, R.drawable.ic_beer)
                super.dismiss()
            }
            boat.setOnClickListener {
                listener.selectIcon(CategoryType.boat.name, R.drawable.ic_boat)
                super.dismiss()
            }
            book.setOnClickListener {
                listener.selectIcon(CategoryType.book.name, R.drawable.ic_book)
                super.dismiss()
            }
            car.setOnClickListener {
                listener.selectIcon(CategoryType.car.name, R.drawable.ic_car)
                super.dismiss()
            }
            clothes.setOnClickListener {
                listener.selectIcon(CategoryType.clothes.name, R.drawable.ic_clothes)
                super.dismiss()
            }
            coffee.setOnClickListener {
                listener.selectIcon(CategoryType.coffee.name, R.drawable.ic_coffe)
                super.dismiss()
            }
            computer.setOnClickListener {
                listener.selectIcon(CategoryType.computer.name, R.drawable.ic_computer)
                super.dismiss()
            }
            cosmetic.setOnClickListener {
                listener.selectIcon(CategoryType.cosmetic.name, R.drawable.ic_cosmetic)
                super.dismiss()
            }
            drink.setOnClickListener {
                listener.selectIcon(CategoryType.drink.name, R.drawable.ic_drink)
                super.dismiss()
            }
            electric.setOnClickListener {
                listener.selectIcon(CategoryType.electric.name, R.drawable.ic_electric)
                super.dismiss()
            }
            entertainment.setOnClickListener {
                listener.selectIcon(CategoryType.entertainment.name, R.drawable.ic_entertainmmment)
                super.dismiss()
            }
            fitness.setOnClickListener {
                listener.selectIcon(CategoryType.fitness.name, R.drawable.ic_fitness)
                super.dismiss()
            }
            food.setOnClickListener {
                listener.selectIcon(CategoryType.food.name, R.drawable.ic_food)
                super.dismiss()
            }
            fruit.setOnClickListener {
                listener.selectIcon(CategoryType.fruit.name, R.drawable.ic_fruit)
                super.dismiss()
            }
            gift.setOnClickListener {
                listener.selectIcon(CategoryType.gift.name, R.drawable.ic_gift)
                super.dismiss()
            }
            grocery.setOnClickListener {
                listener.selectIcon(CategoryType.grocery.name, R.drawable.ic_grocery)
                super.dismiss()
            }
            home.setOnClickListener {
                listener.selectIcon(CategoryType.home.name, R.drawable.ic_home)
                super.dismiss()
            }
            hotel.setOnClickListener {
                listener.selectIcon(CategoryType.hotel.name, R.drawable.ic_hotel)
                super.dismiss()
            }
            iceCream.setOnClickListener {
                listener.selectIcon(CategoryType.iceCream.name, R.drawable.ic_icecream)
                super.dismiss()
            }
            laundry.setOnClickListener {
                listener.selectIcon(CategoryType.laundry.name, R.drawable.ic_laundry)
                super.dismiss()
            }
            medical.setOnClickListener {
                listener.selectIcon(CategoryType.medical.name, R.drawable.ic_medical)
                super.dismiss()
            }
            noodle.setOnClickListener {
                listener.selectIcon(CategoryType.noodle.name, R.drawable.ic_noodle)
                super.dismiss()
            }
            oil.setOnClickListener {
                listener.selectIcon(CategoryType.oil.name, R.drawable.ic_oil)
                super.dismiss()
            }
            other.setOnClickListener {
                listener.selectIcon(CategoryType.other.name, R.drawable.ic_other)
                super.dismiss()
            }
            people.setOnClickListener {
                listener.selectIcon(CategoryType.people.name, R.drawable.ic_people)
                super.dismiss()
            }
            phone.setOnClickListener {
                listener.selectIcon(CategoryType.phone.name, R.drawable.ic_phone)
                super.dismiss()
            }
            pill.setOnClickListener {
                listener.selectIcon(CategoryType.pill.name, R.drawable.ic_pill)
                super.dismiss()
            }
            pizza.setOnClickListener {
                listener.selectIcon(CategoryType.pizza.name, R.drawable.ic_pizza)
                super.dismiss()
            }
            plane.setOnClickListener {
                listener.selectIcon(CategoryType.plane.name, R.drawable.ic_plane)
                super.dismiss()
            }
            restaurant.setOnClickListener {
                listener.selectIcon(CategoryType.restaurant.name, R.drawable.ic_restaurant)
                super.dismiss()
            }
            saving.setOnClickListener {
                listener.selectIcon(CategoryType.saving.name, R.drawable.ic_saving)
                super.dismiss()
            }
            sell.setOnClickListener {
                listener.selectIcon(CategoryType.sell.name, R.drawable.ic_sell)
                super.dismiss()
            }
            shopping.setOnClickListener {
                listener.selectIcon(CategoryType.shopping.name, R.drawable.ic_shopping)
                super.dismiss()
            }
            sweet.setOnClickListener {
                listener.selectIcon(CategoryType.sweet.name, R.drawable.ic_sweet)
                super.dismiss()
            }
            taxi.setOnClickListener {
                listener.selectIcon(CategoryType.taxi.name, R.drawable.ic_taxi)
                super.dismiss()
            }
            train.setOnClickListener {
                listener.selectIcon(CategoryType.train.name, R.drawable.ic_train)
                super.dismiss()
            }
            water.setOnClickListener {
                listener.selectIcon(CategoryType.water.name, R.drawable.ic_water)
                super.dismiss()
            }
            working.setOnClickListener {
                listener.selectIcon(CategoryType.working.name, R.drawable.ic_working)
                super.dismiss()
            }
        }
    }


}