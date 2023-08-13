import axios from "axios";

export default async function handler(req, res) {
  if (req.method !== "POST") {
    res.status(405).end();
  }
  const { headers } = req.headers;
  const { id } = req.body;

  try {
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_AUTH_API_URL}/user/verification`,
      {
        id,
      },
      {
        headers: {
          Authorization: req.headers["authorization"],
        },
      }
    );

    res.status(200).json(response.data);
  } catch (e) {
    res.status(e.response.status).json(e.response.data);
  }
}
