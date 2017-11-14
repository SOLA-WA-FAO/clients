function saveCommunityArea() {
    mapControl.toggleMapEditing(false);
    var communityAreaLayer = mapControl.getMap().getLayer(OT.Map.LAYER_IDS.COMMUNITY_AREA);

    // Do Checks
    if (communityAreaLayer.features.length < 1) {
        alert('Provide community area');
        return false;
    }

    var polygons = [];
    var geom;

    for (var i = 0; i < communityAreaLayer.features.length; i++) {
        var feature = communityAreaLayer.features[i].clone();
        geom = feature.geometry;
        geom.transform(destCrs, sourceCrs);

        if (geom.CLASS_NAME === 'OpenLayers.Geometry.MultiPolygon') {
            for (var j = 0; j < geom.components.length; j++) {
                polygons.push(geom.components[j]);
            }
        } else {
            polygons.push(geom);
        }
    }

    if (polygons.length > 1) {
        geom = new OpenLayers.Geometry.MultiPolygon(polygons);
    }

    $("#mainForm\\:hCommunityArea").val(geom.toString());
    return true;
}