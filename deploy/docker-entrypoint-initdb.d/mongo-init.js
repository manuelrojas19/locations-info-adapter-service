db.createCollection('bankEntity');
db.bankEntity.insertMany([
    {
        "_id": "64186ad0c3428b5847cf9a4d",
        "name": "Centro Histórico",
        "street": "Av. Francisco I. Madero 1",
        "address": "Centro Histórico, Mexico City, C.P. 06000",
        "references": "Av. Francisco I. Madero and Isabel la Católica",
        "state": "CDMX",
        "postalCode": "06000",
        "type": "Branch",
        "phone": "5555123456",
        "openingTime": {
            "open": "09:00",
            "close": "17:00"
        },
        "location": {
            "latitude": 19.433334,
            "longitude": -99.133334
        }
    },
    {
        "_id": "64186ad0c3428b5847cf9a4e",
        "name": "Polanco",
        "street": "Av. Presidente Masaryk 123",
        "address": "Polanco, Mexico City, C.P. 11560",
        "references": "Av. Presidente Masaryk and Tennyson",
        "state": "CDMX",
        "postalCode": "11560",
        "type": "Branch",
        "phone": "5555987654",
        "openingTime": {
            "open": "10:00",
            "close": "18:00"
        },
        "location": {
            "latitude": 19.432603,
            "longitude": -99.200416
        }
    },
    {
        "_id": "64186ad0c3428b5847cf9a4f",
        "name": "Condesa",
        "street": "Av. Tamaulipas 150",
        "address": "Col. Condesa, Mexico City, C.P. 06140",
        "references": "Av. Tamaulipas and Av. México",
        "state": "CDMX",
        "postalCode": "06140",
        "type": "Branch",
        "phone": "5555076789",
        "openingTime": {
            "open": "08:00",
            "close": "15:00"
        },
        "location": {
            "latitude": 19.414571,
            "longitude": -99.172759
        }
    },
    {
        "_id": "64186ad0c3428b5847cf9a50",
        "name": "Coyoacán",
        "street": "Ignacio Allende 36",
        "address": "Col. Del Carmen, Coyoacán, Mexico City, C.P. 04100",
        "references": "Ignacio Allende and Xicoténcatl",
        "state": "CDMX",
        "postalCode": "04100",
        "type": "Branch",
        "phone": "5555764321",
        "openingTime": {
            "open": "09:00",
            "close": "17:00"
        },
        "location": {
            "latitude": 19.349622,
            "longitude": -99.161766
        }
    },
    {
        "_id": "64186ad0c3428b5847cf9a51",
        "name": "Santa Fe",
        "street": "Prolongación Paseo de la Reforma 400",
        "address": "Col. Lomas de Santa Fe, Mexico City, C.P. 01219",
        "references": "Prolongación Paseo de la Reforma and Av. Vasco de Quiroga",
        "state": "CDMX",
        "postalCode": "01219",
        "type": "Branch",
        "phone": "5555234567",
        "openingTime": {
            "open": "10:00",
            "close": "19:00"
        },
        "location": {
            "latitude": 19.366743,
            "longitude": -99.276833
        }
    }
]);