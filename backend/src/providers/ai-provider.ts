export interface TextGenerationRequest {
  readonly input: string;
}

export interface TextGenerationResult {
  readonly text: string;
  readonly provider: "mock";
}

export interface AIProvider {
  generateText(request: TextGenerationRequest): Promise<TextGenerationResult>;
}

/** Development adapter only; it performs no network request and never echoes submitted page content. */
export class MockAIProvider implements AIProvider {
  async generateText(
    request: TextGenerationRequest,
  ): Promise<TextGenerationResult> {
    void request;
    return {
      text: "Mock provider is active. No external AI service was contacted.",
      provider: "mock",
    };
  }
}
