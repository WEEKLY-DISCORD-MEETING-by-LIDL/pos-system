import axios from 'axios';

export const login = async (username, password) => {
  try {
    const response = await axios.post('http://localhost:8080/login', {
      username,
      password
    });

    if (response.data && response.data.token) {
        console.log(response.data.token); //For showcase, wouldn't be in production
      return response.data.token;
    } else {
      throw new Error('Invalid server response: Token not found');
    }
  } catch (error) {
    if (error.response) {
        //Will have to be tested again if it outputs right values after merges
        const errorMsg = error.response.data || `Status: ${error.response.status}`;
        throw new Error(`${errorMsg}`);
    } else {
      throw new Error('Network error or server not reachable');
    }
  }
};
