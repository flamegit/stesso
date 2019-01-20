package com.stesso.android.lib

import chihane.jdaddressselector.AddressProvider
import chihane.jdaddressselector.model.City
import chihane.jdaddressselector.model.County
import chihane.jdaddressselector.model.Province
import chihane.jdaddressselector.model.Street
import com.stesso.android.datasource.net.ApiService
import com.stesso.android.utils.doHttpRequest

class NetAddressProvider(val apiService: ApiService) : AddressProvider {

    override fun provideCitiesWith(provinceId: Int, addressReceiver: AddressProvider.AddressReceiver<City>?) {
        doHttpRequest(apiService.getRegionList(provinceId)) { regionList ->
            val cities = regionList?.map {
                val city = City()
                city.id = it.id ?: 0
                city.name = it.name
                city
            }
            addressReceiver?.send(cities)
        }
    }

    override fun provideCountiesWith(cityId: Int, addressReceiver: AddressProvider.AddressReceiver<County>?) {
        doHttpRequest(apiService.getRegionList(cityId)) { regionList ->
            val counties = regionList?.map {
                val county = County()
                county.id = it.id ?: 0
                county.name = it.name
                county
            }
            addressReceiver?.send(counties)
        }
    }

    override fun provideProvinces(addressReceiver: AddressProvider.AddressReceiver<Province>?) {
        doHttpRequest(apiService.getRegionList(0)) { regionList ->
            val provinces = regionList?.map {
                val province = Province()
                province.id = it.id ?: 0
                province.name = it.name
                province
            }
            addressReceiver?.send(provinces)
        }
    }

    override fun provideStreetsWith(countyId: Int, addressReceiver: AddressProvider.AddressReceiver<Street>?) {
        addressReceiver?.send(null)

    }
}