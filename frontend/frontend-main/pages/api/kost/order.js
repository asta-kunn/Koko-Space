import axios from "axios";

export default async function handler(req, res) {
  if (req.method !== "POST") {
    res.status(405).end();
  }
  const body = req.body;

  const token = req.headers.authorization.split(" ")[1];

  try {
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_KOST_API_URL}/bundle/order/create`,
      {
        ...body,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    res.status(201).json(response.data);
  } catch (e) {
    console.log(e);
    res.status(e.response.status).json(e.response.data);
  }
}
