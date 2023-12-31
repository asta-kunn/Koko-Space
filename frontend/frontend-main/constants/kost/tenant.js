// dummy data
export const TENANT_DATA = [
  {
    id: 0,
    tenantName: "John Doe",
    kostRent: {
      id: 0,
      userId: 2,
      userName: "John Doe",
      roomNumber: 5,
      kostRoom: {
        id: 1,
        name: "Campingski Resort",
        type: "CAMPUR",
        city: "Jakarta",
        country: "Indonesia",
        address: "Jl Kebenaran",
        facilities: ["Kamar mandi", "AC"],
        images: [
          "https://res.cloudinary.com/dkg0oswii/image/upload/v1669102647/cld-sample-4.jpg",
        ],
        stock: 2,
        price: 1200000,
        discount: 12,
        minDiscountDuration: 12,
      },
      checkInDate: "2015-05-10",
      checkOutDate: "2015-08-10",
      duration: 3,
      totalPrice: 3600000,
    },
    roomServices: [
      {
        id: 0,
        userId: 2,
        kostRentId: 1, // ini jd KostRent di backend
        startDate: "2023-05-05",
        endDate: "2023-05-05",
        status: "FINISHED",
      },
      {
        id: 1,
        userId: 2,
        kostRentId: 1,
        startDate: "2023-05-05",
        endDate: "2023-05-05",
        status: "FINISHED",
      },
    ],
  },
  {
    id: 1,
    tenantName: "Super Doe",
    kostRent: {
      id: 1,
      userId: 1,
      userName: "Super Doe",
      roomNumber: 4,
      kostRoom: {
        id: 2,
        name: "Campingski Resort",
        type: "CAMPUR",
        city: "Jakarta",
        country: "Indonesia",
        address: "Jl Kebenaran",
        facilities: ["Kamar mandi", "AC"],

        images: [
          "https://res.cloudinary.com/dkg0oswii/image/upload/v1669102647/cld-sample-4.jpg",
        ],
        stock: 2,
        price: 1200000,
        discount: 12,
        minDiscountDuration: 12,
      },
      checkInDate: "2015-05-10",
      checkOutDate: "2015-08-10",
      duration: 3,
      totalPrice: 3600000,
    },
    roomServices: [
      {
        id: 2,
        userId: 1,
        kostRentId: 1,
        startDate: "2023-05-05",
        endDate: "2023-05-05",
        status: "FINISHED",
      },
      {
        id: 3,
        userId: 1,
        kostRentId: 1,
        startDate: "2023-05-05",
        endDate: "2023-05-05",
        status: "FINISHED",
      },
    ],
  },
];
