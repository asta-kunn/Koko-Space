import axios from "axios";

export default async function handler(req, res) {
  if (req.method !== "POST") {
    res.status(405).end();
  }

  const { rentalName, amount, kuponId } = req.body;
  const token = req.headers.authorization.split(" ")[1];

  try {
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_AUTH_API_URL}/pay`,
      {
        rentalName,
        amount,
        kuponId,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    res.status(200).json(response.data);
  } catch (e) {
    console.log(e.response.data);
    res.status(e.response.status).json(e.response.data);
  }
}
