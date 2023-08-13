import axios from "axios";

export default async function handler(req, res) {
  if (req.method !== "GET") {
    res.status(405).end();
  }

  try {
    const token = req.headers.authorization.split(" ")[1];

    const response = await axios.get(
      `${process.env.NEXT_PUBLIC_AUTH_API_URL}/wallet/history-all`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    res.status(200).json(response.data);
  } catch (e) {
    console.log(e);
    res.status(e.response.status).json(e.response.data);
  }
}
