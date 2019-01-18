package com.stesso.android.lib

import chihane.jdaddressselector.AddressProvider
import chihane.jdaddressselector.model.City
import chihane.jdaddressselector.model.County
import chihane.jdaddressselector.model.Province
import chihane.jdaddressselector.model.Street

class NetAddressProvider : AddressProvider{

    override fun provideCitiesWith(provinceId: Int, addressReceiver: AddressProvider.AddressReceiver<City>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun provideCountiesWith(cityId: Int, addressReceiver: AddressProvider.AddressReceiver<County>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun provideProvinces(addressReceiver: AddressProvider.AddressReceiver<Province>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun provideStreetsWith(countyId: Int, addressReceiver: AddressProvider.AddressReceiver<Street>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}