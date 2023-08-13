import axios from "axios";

export default async function handler(req, res) {
  if (req.method !== "PATCH") {
    res.status(405).end();
  }

  const { newPassword, secretToken } = req.body;

  try {
    const response = await axios.patch(
      `${process.env.NEXT_PUBLIC_AUTH_API_URL}/user/change-password`,
      {
        newPassword,
        secretToken,
      }
    );

    res.status(200).json(response.data);
  } catch (e) {
    res.status(e.response.status).json(e.response.data);
  }
}
