package com.example.arfinance.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.arfinance.R
import com.example.arfinance.databinding.BottomSheetSelectCategoryIconBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CategoryBottomSheetDialog : BottomSheetDialogFragment() {

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
               // linstener.selectIcon("accessory", R.drawable.ic_accessory)
                super.dismiss()
            }
            award.setOnClickListener {

                super.dismiss()
            }
            beer.setOnClickListener {

                super.dismiss()
            }
            boat.setOnClickListener {

                super.dismiss()
            }
            book.setOnClickListener {

                super.dismiss()
            }
            car.setOnClickListener {

                super.dismiss()
            }
            clothes.setOnClickListener {

                super.dismiss()
            }
            coffee.setOnClickListener {

                super.dismiss()
            }
            computer.setOnClickListener {

                super.dismiss()
            }
            cosmetic.setOnClickListener {

                super.dismiss()
            }
            drink.setOnClickListener {

                super.dismiss()
            }
            electric.setOnClickListener {

                super.dismiss()
            }
            entertainment.setOnClickListener {

                super.dismiss()
            }
            fitness.setOnClickListener {

                super.dismiss()
            }
            food.setOnClickListener {

                super.dismiss()
            }
            fruit.setOnClickListener {

                super.dismiss()
            }
            gift.setOnClickListener {

                super.dismiss()
            }
            grocery.setOnClickListener {

                super.dismiss()
            }
            home.setOnClickListener {

                super.dismiss()
            }
            hotel.setOnClickListener {

                super.dismiss()
            }
            iceCream.setOnClickListener {

                super.dismiss()
            }
            laundry.setOnClickListener {

                super.dismiss()
            }
            medical.setOnClickListener {

                super.dismiss()
            }
            noodle.setOnClickListener {

                super.dismiss()
            }
            oil.setOnClickListener {

                super.dismiss()
            }
            other.setOnClickListener {

                super.dismiss()
            }
            people.setOnClickListener {

                super.dismiss()
            }
            phone.setOnClickListener {

                super.dismiss()
            }
            pill.setOnClickListener {

                super.dismiss()
            }
            pizza.setOnClickListener {

                super.dismiss()
            }
            plane.setOnClickListener {

                super.dismiss()
            }
            restaurant.setOnClickListener {

                super.dismiss()
            }
            saving.setOnClickListener {

                super.dismiss()
            }
            sell.setOnClickListener {

                super.dismiss()
            }
            shopping.setOnClickListener {

                super.dismiss()
            }
            sweet.setOnClickListener {

                super.dismiss()
            }
            taxi.setOnClickListener {

                super.dismiss()
            }
            train.setOnClickListener {

                super.dismiss()
            }
            water.setOnClickListener {

                super.dismiss()
            }
            working.setOnClickListener {

                super.dismiss()
            }
        }
    }


}