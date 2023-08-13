import axios from "axios";

export default async function handler(req, res) {
  if (req.method !== "POST") {
    res.status(405).end();
  }

  const { name, email, password } = req.body;

  try {
    console.log("handler try")
    const response = await axios.post(
      `${process.env.NEXT_PUBLIC_AUTH_API_URL}/user/register`,
      {
        name,
        email,
        password,
      }
    );
    res.status(200).json(response.data);
  } catch (e) {
    console.log("handler catch")
    console.log(e.response.data);
    console.log()
    res.status(e.response.status).json(e.response.data);
  }
}
