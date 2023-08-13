import axios from "axios";

export default async function handler(req, res) {
  const { id } = req.query;

  try {
    const response = await axios.get(
      `${process.env.NEXT_PUBLIC_AUTH_API_URL}/coupon/get/user/` + id
    );
    res.status(200).json(response.data);
  } catch (e) {
    console.log(e.response.data);
    res.status(e.response.status).json(e.response.data);
  }
}
