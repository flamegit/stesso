package chihane.jdaddressselector;

import android.content.Context;

import java.util.List;

import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;

public class DefaultAddressProvider implements AddressProvider {
    AddressApi mAddressProvider;
    public DefaultAddressProvider(Context context) {
        mAddressProvider=new AddressApi(context);
    }

    @Override
    public void provideProvinces(final AddressReceiver<Province> addressReceiver) {
        List<Province> provinces=mAddressProvider.getProvinces();
        addressReceiver.send(provinces);
    }

    @Override
    public void provideCitiesWith(int provinceId, final AddressReceiver<City> addressReceiver) {
        List<City> cities=mAddressProvider.getCities(provinceId);
        addressReceiver.send(cities);
    }

    @Override
    public void provideCountiesWith(int cityId, final AddressReceiver<County> addressReceiver) {
        List<County> counties=mAddressProvider.getCountries(cityId);
        addressReceiver.send(counties);
    }

    @Override
    public void provideStreetsWith(int countyId, final AddressReceiver<Street> addressReceiver) {
//        final FlowQueryList<Street> streetQueryList = SQLite.select()
//                .from(Street.class)
//                .where(Street_Table.county_id.eq(countyId))
//                .flowQueryList();
        addressReceiver.send(null);
    }
}
