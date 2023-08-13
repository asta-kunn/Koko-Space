export const NAV_ROUTE = [
  { id: 1, href: "/", name: "Home" },
  {
    id: 2,
    href: "",
    name: "Kost",
    dropdownMenu: [
      { href: "/kost/menu", name: "Menu" },
      { href: "/CleaningService/memesan", name: "Memesan cleaning service" },
      { href: "/CleaningService/monitoring", name: "Monitoring cleaning service" },
      { href: "/bundle/menu", name: "Bundle Menu" },
      { href: "/kost/teman_menginap", name: "Teman Menginap" },
      { href: "/kost/rentals", name: "View My Rental" },
      { href: "/kost/occupancy", name: "Occupancy" },
    ],
  },
  {
    id: 3,
    href: "",
    name: "Coworking space",
    dropdownMenu: [
      { href: "/coworkingspace/menu", name: "Menu" },
      { href: "/coworkingspace/inforent", name: "Informasi Penyewaan" },
      { href: "/admincoworking/adminmenu", name: "Admin" },
    ],
  },
  {
    id: 4,
    href: "",
    name: "Auth",
    dropdownMenu: [
      { href: "/auth/register", name: "Register" },
      { href: "/auth/login", name: "Login" },
    ],
  },
  {
    id: 5,
    href: "",
    name: "Meeting Room",
    dropdownMenu: [
      { href: "/room/roommenu", name: "Rent" },
      { href: "/adminroom/adminmenu", name: "Admin" },
      { href: "/roomrent/me", name: "View My Rentals" },
    ],
  },
  {
    id: 6,
    href: "",
    name: "Wallet",
    dropdownMenu: [
      { href: "/wallet/", name: "Manage Wallet" },
      { href: "/wallet/history", name: "Purchase History" },
      { href: "/wallet/topup", name: "Top Up " },
    ],
  },
  {
    id: 7,
    href: "",
    name: "Account",
    dropdownMenu: [
      { href: "/auth/verification", name: "Account Verification" },
      { href: "/coupon/MyCoupon", name: "My Coupon" },
    ],
  },
];
