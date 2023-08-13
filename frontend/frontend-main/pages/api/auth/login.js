import axios from "axios";

export default async function handler(req, res) {
  if (req.method !== "POST") {
    res.status(405).end();
  }

  const { email, password } = req.body;

  try {
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_AUTH_API_URL}/user/login`,
      {
        email,
        password,
      }
    );
    res.status(200).json(response.data);
  } catch (e) {
    console.log(e.response);
    res.status(e.response.status).json(e.response.data);
  }
}
