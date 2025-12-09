from abc import ABC, abstractmethod

class TextJustifier(ABC):

    @abstractmethod
    def justify(self, text: str, max_width: int) -> list[str]:
        pass

    def _split_words(self, text: str) -> list[str]:
        return text.split()