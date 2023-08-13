import axios from "axios";

export default async function handler(req, res) {
  if (req.method !== "POST") {
    res.status(405).end();
  }

  const { walletHistoryId, accepted } = req.body;
  const token = req.headers.authorization.split(" ")[1];

  try {
    console.log(walletHistoryId);
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_AUTH_API_URL}/wallet/verify-topup`,
      {
        walletHistoryId,
        accepted,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    res.status(200).json(response.data);
  } catch (e) {
    console.log(e.response);
    res.status(e.response.status).json(e.response.data);
  }
}
