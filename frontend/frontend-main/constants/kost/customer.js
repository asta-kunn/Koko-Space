// dummy data
export const CUSTOMER_INFO = [
  {
    customerId: 1,
    customerName: "Scott Lang",
    customerPicture:
      "https://upload.wikimedia.org/wikipedia/en/thumb/8/88/Paul_Rudd_as_Ant-Man.jpg/250px-Paul_Rudd_as_Ant-Man.jpg",
    rentalInformations: [
      {
        rentalId: 1,
        roomNumber: 27,
        startDate: "10/01/2023",
        endDate: "10/02/2023",
        additionalServices: {
          "Room Cleaning": 2,
        },
        kostName: "Rose Kost",
        kostPicture:
          "https://res.cloudinary.com/dkg0oswii/image/upload/v1669102646/cld-sample-4.jpg",
        location: "Jakarta, Indonesia",
      },
      {
        rentalId: 3,
        roomNumber: 33,
        startDate: "02/02/2023",
        endDate: "02/03/2023",
        additionalServices: {
          "Room Cleaning": 3,
          "AC Service": 1,
        },
        kostName: "Daisy Kost",
        kostPicture:
          "https://res.cloudinary.com/dkg0oswii/image/upload/v1669102647/cld-sample-5.jpg",
        location: "Depok, Indonesia",
      },
    ],
  },
  {
    customerId: 3,
    customerName: "Kang",
    customerPicture:
      "https://media.distractify.com/brand-img/nZOhCdPxJ/0x0/how-strong-is-kang-the-conqueror-1676580318157.jpg",
    rentalInformations: [
      {
        rentalId: 2,
        roomNumber: 33,
        startDate: "02/02/2023",
        endDate: "02/03/2023",
        additionalServices: {
          "Room Cleaning": 2,
        },
        kostName: "Rose Kost",
        kostPicture:
          "https://res.cloudinary.com/dkg0oswii/image/upload/v1669102646/cld-sample-4.jpg",
        location: "Jakarta, Indonesia",
      },
    ],
  },
];
